import { string } from "prop-types"
import axios from 'axios'

export const getSchedules = () => {
    // communicates with api and returns array of schedule combinations
    // each schedule is composed of a list of courses
    return new Promise((resolve, reject) => {
        axios({
            // parameters for get request to server
        })
        .then(response => {
            // if server call is successfull, generates calendar compatible events and generates a new promise with the events
            resolve(generateEvents(response.data.schedules))
        })
        .catch(error => {
            // if server responded with an error, handle this
            reject(error)
        })
    })
}

const DATE_CONVERTER = {
    'M': '2020-12-08',
    'T': '2020-12-09',
    'W': '2020-12-10',
    'H': '2020-12-11',
    'F': '2020-12-12'
}

const schedulesMock = [
    {
        decidedClasses: [
            {
                department: 'CSCI',
                courseNumber: 201,
                daysOfWeek: 'MWF',
                startTime: '18:00',
                endTime: '19:00',
                sectionType: 'Lec'
            },
            {
                department: 'CSCI',
                courseNumber: 270,
                daysOfWeek: 'TH',
                startTime: '15:00',
                endTime: '18:00',
                sectionType: 'Lec'
            },
            {
                department: 'Stupid-GE',
                courseNumber: 100,
                daysOfWeek: 'MWF',
                startTime: '09:00',
                endTime: '10:00',
                sectionType: 'Lec'
            }
        ]
    }
]
export const getSchedules2 = () => {
    return generateEvents(schedulesMock)
}

// helper function for getSchedules
const generateEvents = (schedules) => {
    // parses schedules in order to convert schedules into lists of events that are able to serve as input to calendar component
    const eventSchedules = []
    schedules.forEach(schedule => {
        const events = [];
        let id = 0;
        schedule.decidedClasses.forEach(section => {
            for(let i = 0; i < section.daysOfWeek.length; i++) {
                const start = new Date(DATE_CONVERTER[section.daysOfWeek[i]])
                start.setHours(section.startTime.split(':')[0], section.startTime.split(':')[1], 0);
                console.log(start)
                const end = new Date(DATE_CONVERTER[section.daysOfWeek[i]])
                end.setHours(section.endTime.split(':')[0], section.endTime.split(':')[1], 0);
                console.log(end)
                const title = section.department + section.courseNumber.toString() + '-' + section.sectionType;
                events.push({
                    id,
                    title,
                    start: start,
                    end: end
                })
                id++;
            }
        })
        eventSchedules.push(events)
    })
    return eventSchedules
}