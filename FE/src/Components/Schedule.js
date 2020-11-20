import { useState, useEffect } from 'react';
import ReactDOM from 'react-dom';
import FullCalendar from '@fullcalendar/react';
import timeGridPlugin from '@fullcalendar/timegrid';
import { getSchedules2 } from '../Helpers/getSchedules';
import Button from 'react-bootstrap/Button';
import Popover from'react-bootstrap/Popover'
import OverlayTrigger from 'react-bootstrap/OverlayTrigger';
import Overlay from 'react-bootstrap/Overlay';

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

    const handleSelectFavoriteSchedule = () => {
        
    }
    

    return (
        <div>
            Select this schedule as your favorite! <Button variant='primary' onClick={handleSelectFavoriteSchedule}>Select</Button>
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
                        text: 'Next Schedule',
                        click: handleRightClick
                    },
                    left: {
                        text: 'Prev Schedule',
                        click: handleLeftClick
                    }
                }}
                headerToolbar = {{
                    left: '',
                    right: '',
                    title: 'title',
                    center: 'left right'
                }}
            />
        </div>
    );
}

export default Schedule;