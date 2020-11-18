import React, {useState, createContext, useContext, useEffect} from 'react';

const AuthContext = createContext();
const AuthProvider = (props) => {
    const [auth, setAuth] = useState(false)

    let checkIsLogined = async () => {
        await fetch("http://localhost:8080/cs201/api/session", {
            method: "POST",
        }).then(response => response.json())
        .then(data => {
            // console.log(data);
            if (data.status === "success"){
                setAuth(true);
            }
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

    const signUp = (data) => {
        // sends form data to signup endpoint of server
        // if successful, sets auth to true
        fetch("http://localhost:8080/cs201/api/users",{
            method:"POST",
            body: data,
            })
            .then(resp => resp.json())
            .then(data => {
                console.log(data);
            });
        setAuth(true);

    }

    const signIn = async (formdata) => {
        // sends form data to signin endpoint of server
        // if successful, sets auth to true
        let msg = null;

        await fetch("http://localhost:8080/cs201/api/session", {
            method: "POST",
            body: formdata,
        }).then(response => response.json())
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
            });
        return msg;
    }

    const signOut = async () => {
        // sets auth to false and if necessary, communicates with signout endpoint of api to sign out
        // let fName = "";
        await fetch("http://localhost:8080/cs201/api/session", {
            method: "DELETE",
            credentials: 'include',
        })
            // .then(data => data.json())
            // .then(data => {
            //     console.log(data);
                // fName = data.user;
            // });
        // alert("Bye!" + fName);
        alert("Bye!");
        setAuth(false);
    }

    // All components that nested under this component will have access to these elements, and will be able to call these functions
    return <AuthContext.Provider value={{auth, signUp, signIn, signOut}} {...props}/>;
}

// useAuth returns an object containing the elements in the provider
const useAuth = () => useContext(AuthContext);
export { AuthProvider, useAuth };

// to use this provider, a component must import useAuth and call it as a function for it to return an object