import { useState, useEffect, useRef } from 'react';
import { debounce } from "lodash";
import axios from 'axios';
import { API_URL } from '../env'
import Select from 'react-select';
import Button from 'react-bootstrap/Button';
import { generateSchedules } from '../Helpers/getSchedules';
const AddCourseForm = props => {
    const [ query, setQuery ] = useState("");
    const [ options, setOptions ] = useState([]);
    const [ selectedCourses, setSelectedCourses ] = useState([]);
    const [ loading, setLoading ] = useState(false);
    const attemptQuery = useRef(
        debounce((query, token) => {
            queryCourses(query, token);
        }, 500)
    ).current

    const queryCourses = (query, cancelToken) => {
        if (!query) {
            setOptions([]);
            return;
        }
        setLoading(true);
        let department = query.match('[^0-9]+')
        let courseNumber = query.match('[0-9]+[^0-9]*')
        if (department != null) department = department[0].toUpperCase();
        if (courseNumber != null) courseNumber = courseNumber[0].toUpperCase();
        const url = new URL(API_URL + 'query');
        url.search = new URLSearchParams({ department, courseNumber });
        axios.get(url, {
            cancelToken
        }).then(response => {
            const courses = response.data;
            const courseOptions = courses.map(course => {
                const name = course.department.toUpperCase() + course.courseNumber.toString();
                return {
                    label: name,
                    value: name
                }
            });
            setOptions(courseOptions);
            setLoading(false);
        })
        .catch(error => {
            if (!axios.isCancel(error)) {
                setOptions([]);
                setLoading(false);
            }
        })
    }

    const handleInputChange = input => {
        setQuery(input);
        return input;
    }

    useEffect(() => {
        const { cancel, token } = axios.CancelToken.source();
        attemptQuery(query, token);
        // return () => cancel("Irrelevant request") || attemptQuery.cancel();
        return () => cancel("Irrelevant request");
    }, [ attemptQuery, query ]);

    const handleChange = courses => {
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
        .then(schedules => {
            localStorage.setItem("schedules", JSON.stringify(schedules));
            const allEvents = generateSchedules(schedules);
            localStorage.setItem("events", JSON.stringify(allEvents));
            props.show(false);
        })
        .catch(error => {
            console.log(error);
        })
    }

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


    return (
        <div style={{margin: 'auto'}}>
            <Select 
                isLoading={loading} 
                styles={styles} 
                onChange={handleChange} 
                onInputChange={handleInputChange} 
                options={options} 
                isMulti
                placeholder='Enter course name e.g. csci201'
                // menuIsOpen={options.length ? true : false}
            />
            <div>
                <Button block style={{width: '30%', margin: '22px auto', minWidth: '30%'}} variant="outline-danger" onClick={createSchedules} >GENERATE SCHEDULES</Button>
            </div>
        </div>
    );
}

export default AddCourseForm;