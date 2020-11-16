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

// helper function for getSchedules
const generateEvents = (schedules) => {
    // parses schedules in order to convert schedules into lists of events that are able to serve as input to calendar component
}