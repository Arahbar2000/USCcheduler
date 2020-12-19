import axios from 'axios';

const DATE_CONVERTER = {
    'M': '2020-12-08',
    'T': '2020-12-09',
    'W': '2020-12-10',
    'H': '2020-12-11',
    'F': '2020-12-12'
}


const colors = ['blue', 'green', 'purple', 'orange', 'brown']
// helper function for getSchedules
export const generateSchedules = (schedules, flag=true) => {
    // parses schedules in order to convert schedules into lists of events that are able to serve as input to calendar component
    const eventSchedules = []
    schedules.forEach(schedule => {
        const events = [];
        let colorIndex = 0;
        const courseColors = {}
        schedule.forEach(section => {
            console.log("processing schedules")
            for(let i = 0; i < section.daysOfWeek.length; i++) {
                const id = section.sectionId;
                const start = new Date(DATE_CONVERTER[section.daysOfWeek[i]])
                const end = new Date(DATE_CONVERTER[section.daysOfWeek[i]])
                if (flag) {
                    start.setHours(section.startTime.hour, section.startTime.minute, 0);
                    end.setHours(section.endTime.hour, section.endTime.minute, 0);
                }
                else {
                    start.setHours(parseInt(section.startTime.split(":")[0]), parseInt(section.startTime.split(":")[1]))
                    end.setHours(parseInt(section.endTime.split(":")[0]), parseInt(section.endTime.split(":")[1]))
                }
                const courseName = section.department + section.courseNumber.toString();
                let color = null;
                if (courseName in courseColors) color = courseColors[courseName];
                else {
                    color = colors[colorIndex++ % colors.length];
                    courseColors[courseName] = color;
                }
                const title = courseName+ '-' + section.sectionType + '-' + section.sectionId;
                events.push({ id, title, start, end, color })
            }
        })
        eventSchedules.push(events)
    })
    return eventSchedules
}