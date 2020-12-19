import { useState, useRef } from 'react';
import debounce from 'debounce-promise';
import { API_URL } from '../env'
import Button from 'react-bootstrap/Button';
import { generateSchedules } from '../Helpers/getSchedules';
import AsyncSelect from 'react-select/async';
import axios from 'axios';

const styles = {
    container: (provided, state) => ({
        ...provided,
        width: '75%',
        margin: 'auto',
    }),
    menu: (provided, state) => ({
        ...provided,
        zIndex: 10,
        marginTop: 0
    })
}

const queryCourses = async query => {
    return new Promise(resolve => {
        if (!query) {
            return resolve([])
        }
        let department = query.match('[^0-9]+')
        let courseNumber = query.match('[0-9]+[^0-9]*')
        if (department != null) department = department[0].toUpperCase();
        if (courseNumber != null) courseNumber = courseNumber[0].toUpperCase();
        const url = new URL(API_URL + 'query');
        url.search = new URLSearchParams({ department, courseNumber });
        axios.get(url).then(response => {
            const courses = response.data
            const courseOptions = courses.map(course => {
                const name = course.department.toUpperCase() + course.courseNumber.toString();
                return {
                    label: name + ': ' + course.title,
                    value: name
                }
            });
            return resolve(courseOptions);
        })
        .catch(error => {
            console.log(error);
            return resolve([]);
        })
    })
}

const AddCourseForm = props => {
    const [ selectedCourses, setSelectedCourses ] = useState([]);

    const loadOptions = useRef(debounce(async (query, callback) => {
        const courseOptions = await queryCourses(query);
        callback(courseOptions);
    }, 500)).current

    const handleChange = courses => {
        if (courses != null) {
            courses.map(course => {
                course.label = course.value
                return course;
            });
        }
        setSelectedCourses(courses);
    }

    const createSchedules = () => {
        const extracurriculum = null;
        let courses = "";
        if (selectedCourses) {
        selectedCourses.forEach(course => {
            courses += course.value + ',';
        })
        courses = courses.slice(0, -1);
        }
        const startTime = localStorage.getItem("startTime");
        const endTime = localStorage.getItem("endTime");
        const url = new URL(API_URL + 'guest');
        url.search = new URLSearchParams({ courses, extraCurriculum: extracurriculum, startTime, endTime });
        fetch(url, {
            method: 'GET',
            credentials: 'include'
        })
        .then(response => response.json())
        .then(response => {
            const schedules = response.schedule
            localStorage.setItem("schedules", JSON.stringify(schedules));
            const allEvents = generateSchedules(schedules);
            localStorage.setItem("events", JSON.stringify(allEvents));
            props.show(false);
        })
        .catch(error => {
            console.log(error);
        })
    }


    return (
        <div style={{margin: 'auto'}}>
            <AsyncSelect 
                styles={styles} 
                onChange={handleChange}
                loadOptions={loadOptions}
                isMulti
                placeholder='Enter course name e.g. csci201'
                defaultOptions={defaultOptions}
            />
            <div>
                <Button block style={{width: '30%', margin: '22px auto', minWidth: '30%'}} variant="outline-danger" onClick={createSchedules} >GENERATE SCHEDULES</Button>
            </div>
        </div>
    );
}

export default AddCourseForm;