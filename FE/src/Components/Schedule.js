import { useState, useEffect } from 'react';
import FullCalendar from '@fullcalendar/react';
import timeGridPlugin from '@fullcalendar/timegrid'
import { getSchedules2 } from '../Helpers/getSchedules'

const Schedule = () => {
    let schedules = getSchedules2();
    const [scheduleIndex, setIndex] = useState(0)
    const handleRightClick = () => {
        if(scheduleIndex < schedules.length - 1) {
            setIndex(scheduleIndex + 1)
        }
        else {
            setIndex(0)
        }
    }
    const handleLeftClick = () => {
        if(scheduleIndex > 0) {
            setIndex(scheduleIndex - 1)
        }
        else {
            setIndex(schedules.length - 1)
        }
    }
    return (
        <FullCalendar
            schedulerLicenseKey={'CC-Attribution-NonCommercial-NoDerivatives'}
            plugins={[ timeGridPlugin ]}
            initialView={'timeGridWeek'}
            headerToolbar={false}
            initialDate={'2020-12-07'}
            hiddenDays={[0, 6]}
            dayHeaderFormat={{weekday: 'long'}}
            events={schedules[scheduleIndex]}
            allDaySlot={false}
            slotMinTime={'07:00:00'}
            slotMaxTime={'22:00:00'}
            customButtons={{
                right: {
                    text: 'Next',
                    click: handleRightClick
                },
                left: {
                    text: 'Prev',
                    click: handleLeftClick
                }
            }}
            headerToolbar = {{
                left: 'left',
                right: 'right',
                title: 'title'
            }}
        />
    );
}

export default Schedule;