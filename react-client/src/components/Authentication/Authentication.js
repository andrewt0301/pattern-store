import {API_PATH_AUTH} from "../../services/common";

export const userService = {
    login,
    logout
};

function login(username, password) {
    const rqOptions = {
        method: "POST",
        mode: "cors",
        headers: {
            "Authorization": "Basic " + window.btoa(`${username}:${password}`)
        },
    };
    return fetch(`${API_PATH_AUTH}users/authenticate`, rqOptions)
        .then(handleResponse)
        .then(user => {
            if (user) {
                let userFront = window.btoa(username + ":" + password);
                localStorage.setItem("user", JSON.stringify(userFront));
                localStorage.setItem("username", JSON.stringify(user.username));
                localStorage.setItem("userId", JSON.stringify(user.id));
                localStorage.setItem("userRole", JSON.stringify(user.role));
            }
            return user;
        });
}

function logout() {
    localStorage.removeItem("user");
    localStorage.removeItem("username");
    localStorage.removeItem("userRole");
    localStorage.removeItem("userId");
}

function handleResponse(response) {
    return response.text().then(text => {
        const data = text && JSON.parse(text);
        if (!response.ok) {
            if (response.status === 401) {
                logout();
            }
            const error = (data && data.error) || response.status;
            return Promise.reject(error);
        }
        return data;
    });
}
