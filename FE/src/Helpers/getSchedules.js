const DATE_CONVERTER = {
    'M': '2020-12-08',
    'T': '2020-12-09',
    'W': '2020-12-10',
    'H': '2020-12-11',
    'F': '2020-12-12'
}


const colors = ['blue', 'green', 'purple', 'red', 'orange', 'brown']
// helper function for getSchedules
export const generateSchedules = (schedules, flag=true) => {
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
                const end = new Date(DATE_CONVERTER[section.daysOfWeek[i]])
                if (flag) {
                    start.setHours(section.startTime.hour, section.startTime.minute, 0);
                    end.setHours(section.endTime.hour, section.endTime.minute, 0);
                }
                else {
                    start.setHours(parseInt(section.startTime.split(":")[0]), parseInt(section.startTime.split(":")[1]))
                    end.setHours(parseInt(section.endTime.split(":")[0]), parseInt(section.endTime.split(":")[1]))
                }
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