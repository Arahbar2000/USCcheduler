import React, {useState, createContext, useContext, useEffect} from 'react';

const UserContext = createContext();
const UserProvider = () => {
    const [userData, setUser] = useState({user: null, error: false});

    useEffect(() => {
        // this function is called everytime the browser is refreshed
        getUser();
    });

    const getUser = () => {
        // gets the user information from the server database
        // must be logged on in order to communicate with database
    }

    // All components that nested under this component will have access to these elements, and will be able to call these functions
    return <UserContext.Provider value={{userData, getUser}}/>;
}

// useAuth returns an object containing the elements in the provider
const useUser = () => useContext(UserContext);
export { UserProvider, useUser };

// to use this provider, a component must import useAuth and call it as a function for it to return an object