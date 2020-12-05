import { useState, useEffect, useRef } from 'react';
import { debounce } from "lodash";
import axios from 'axios';
import { API_URL } from '../env'
const AddCourseForm = props => {
    const [ query, setQuery ] = useState("");

    const handleChange = event => {
        setQuery(event.target.value);
    }

    const attemptQuery = useRef(
        debounce((query, token) => {
            queryCourses(query, token);
        }, 500)
    ).current;

    const queryCourses = (query, cancelToken) => {
        if (!query) return;
        const department = query.match('[^0-9]+')[0].toUpperCase();
        const courseNumber = parseInt(query.match('[0-9]+'));
        console.log(courseNumber);
        const url = new URL(API_URL + 'query');
        url.search = new URLSearchParams({ department, courseNumber });
        console.log(url);
        axios.get(url, {
            cancelToken
        }).then(response => {
            const courses = response.data;
        })
        .catch(error => {
            axios.isCancel(error) || console.log(error);
        })
    }

    useEffect(() => {
        const { cancel, token } = axios.CancelToken.source();
        attemptQuery(query, token);
        return () => cancel("Irrelevant request") || queryCourses.cancel();
    }, [ attemptQuery, query ]);

    return (
        <input onChange={handleChange} type='text' />
    );
}

export default AddCourseForm;