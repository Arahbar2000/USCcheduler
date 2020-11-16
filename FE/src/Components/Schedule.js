import FullCalendar from '@fullcalendar/react';
import timeGridPlugin from '@fullcalendar/timegrid'

const Schedule = () => {
    return (
        <FullCalendar
            schedulerLicenseKey={'CC-Attribution-NonCommercial-NoDerivatives'}
            plugins={[ timeGridPlugin ]}
            initialView={'timeGridWeek'}
            headerToolbar={false}
        />
    );
}

export default Schedule;