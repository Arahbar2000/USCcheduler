import { useState, useEffect } from 'react';
import FullCalendar from '@fullcalendar/react';
import timeGridPlugin from '@fullcalendar/timegrid'
import { getSchedules2 } from '../Helpers/getSchedules'

const Schedule = () => {
    // useEffect(() => {
    //     if (localStorage.getItem('schedules') == null) {

    //         localStorage.setItem('schedules') = getSchedules2()
    //     }
    //     else {

    //     }
    // })
    let schedules = getSchedules2();
    console.log(schedules)
    const [selectedSchedule, setSchedule] = useState(schedules[0])
    console.log(selectedSchedule)
    return (
        <FullCalendar
            schedulerLicenseKey={'CC-Attribution-NonCommercial-NoDerivatives'}
            plugins={[ timeGridPlugin ]}
            initialView={'timeGridWeek'}
            headerToolbar={false}
            initialDate={'2020-12-07'}
            hiddenDays={[0, 6]}
            dayHeaderFormat={{weekday: 'long'}}
            events={selectedSchedule}
            allDaySlot={false}
            slotMinTime={'07:00:00'}
            slotMaxTime={'22:00:00'}
        />
    );
}

export default Schedule;