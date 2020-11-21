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
            resolve(generateSchedules(response.data.schedules))
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
    },
    {
        decidedClasses: [
            {
                department: 'CSCI',
                courseNumber: 201,
                daysOfWeek: 'MWF',
                startTime: '12:00',
                endTime: '14:00',
                sectionType: 'Lec'
            },
            {
                department: 'CSCI',
                courseNumber: 270,
                daysOfWeek: 'TH',
                startTime: '14:00',
                endTime: '15:00',
                sectionType: 'Lec'
            },
            {
                department: 'Stupid-GE',
                courseNumber: 100,
                daysOfWeek: 'MWF',
                startTime: '010:00',
                endTime: '11:00',
                sectionType: 'Lec'
            }
        ]
    }
]

const colors = ['blue', 'green', 'purple', 'red', 'orange', 'brown']
// helper function for getSchedules
export const generateSchedules = (schedules) => {
    // parses schedules in order to convert schedules into lists of events that are able to serve as input to calendar component
    const eventSchedules = []
    schedules.forEach(schedule => {
        const events = [];
        let colorIndex = 0;
        schedule.forEach(section => {
            console.log("processing schedules")
            for(let i = 0; i < section.daysOfWeek.length; i++) {
                const id = section.sectionId;
                const start = new Date(DATE_CONVERTER[section.daysOfWeek[i]])
                start.setHours(section.startTime.hour, section.startTime.minute, 0);
                const end = new Date(DATE_CONVERTER[section.daysOfWeek[i]])
                end.setHours(section.endTime.hour, section.endTime.minute, 0);
                const title = section.department + section.courseNumber.toString() + '-' + section.sectionType + '-' + section.sectionId;
                events.push({
                    id,
                    title,
                    start: start,
                    end: end,
                    color: colors[colorIndex % colors.length]
                })
            }
            colorIndex++;
        })
        eventSchedules.push(events)
    })
    return eventSchedules
}