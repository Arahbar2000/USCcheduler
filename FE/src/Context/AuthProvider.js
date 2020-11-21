import React, {useState, createContext, useContext, useEffect} from 'react';
import Swal from 'sweetalert2';
import { API_URL } from '../env'

const AuthContext = createContext();
const AuthProvider = (props) => {
    const [auth, setAuth] = useState(false)

    let checkIsLogined = async () => {
        await fetch(API_URL + "session", {
            method: "POST",
            credentials: 'include'
        }).then(response => response.json())
        .then(data => {
            console.log(data);
            if (data.status === "success"){
                setAuth(true);
                console.log('is logged in');
            }
        })
        .catch(error => {
            console.log(error);
        });
    }

    useEffect(() => {
        // this function is called everytime the browser is refreshed
        // checks if the user is already logged in
        // if already logged in, set auth to true
        if (!auth){
            checkIsLogined();
        }
        // cleanup auth
        // return () => setAuth(false);
    });

    const signUp = ({ fname, lname, email, password }) => {
        // sends form data to signup endpoint of server
        // if successful, sets auth to true
        const url = new URL(API_URL + 'users');
        url.search = new URLSearchParams({fname, lname, email, password})
        fetch(url,{
            method:"POST"
            })
            .then(resp => resp.json())
            .then(data => {
                console.log(data)
                Swal.fire({
                    icon: 'success',
                    text: 'welcome!'
                });
                setAuth(true);
            })
            .catch(error => {
                console.log(error);
            });
    }

    const signIn = async ({ email, password }) => {
        // sends form data to signin endpoint of server
        // if successful, sets auth to true
        let msg = null;
        const url = new URL(API_URL + "session");
        console.log(email, password)
        url.search = new URLSearchParams({ email, password })
        await fetch(url, {
            method: "POST",
            credentials: 'include'
            })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                if (data.status === "success") {
                    console.log("setting auth  = true");
                    setAuth(true);
                }
                else{
                    msg = data.message;
                    setAuth(false);
                }
            })
            .catch(error => {
                console.log(error);
            });
        return msg;
    }

    const signOut = async () => {
        // sets auth to false and if necessary, communicates with signout endpoint of api to sign out
        // let fName = "";
        await fetch(API_URL + "session", {
            method: "DELETE",
            credentials: 'include',
        })
        .then(() => {
            Swal.fire("Bye!");
            setAuth(false);
        });
    }

    // All components that nested under this component will have access to these elements, and will be able to call these functions
    return <AuthContext.Provider value={{auth, signUp, signIn, signOut}} {...props}/>;
}

// useAuth returns an object containing the elements in the provider
const useAuth = () => useContext(AuthContext);
export { AuthProvider, useAuth };

// to use this provider, a component must import useAuth and call it as a function for it to return an object