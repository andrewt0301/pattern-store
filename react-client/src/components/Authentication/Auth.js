import {API_PATH, API_PATH_ADMIN} from "../../services/common";
import {AuthHeader} from "../AuthHeader/AuthHeader";

export const userService = {
    login,
    logout,
    getAll
};

function login(username, password) {
    const rqOptions = {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({username, password})
    };

    return fetch(`${API_PATH}/users/authenticate`, rqOptions)
        .then(handleResponse)
        .then(user => {
            if (user) {
                user.authdata = window.btoa(username + ":" + password);
                localStorage.setItem("user", JSON.stringify(user));
            }
            return user;
        });
}

function logout() {
    localStorage.removeItem("user");
}

function getAll() {
    const rqOptions = {
        method: "GET",
        headers: AuthHeader()
    };

    return fetch(`${API_PATH_ADMIN}/users`, rqOptions).then(handleResponse);
}

function handleResponse(response) {
    return response.text().then(text => {
        const data = text && JSON.parse(text);
        if (!response.ok) {
            if (response.status === 401) {
                logout();
                // location.reload();
            }
            const error = (data && data.message) || response.statusText;
            return Promise.reject(error);
        }
        return data;
    });
}
