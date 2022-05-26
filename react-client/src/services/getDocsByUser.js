import {API_PATH, API_PATH_AUTH} from "./common";

export function getDocsByUser() {
    let obj = {
        method: "GET",
        mode: "cors",
        headers: {
            "Accept": "application/json",
            "Authorization": "Basic " + JSON.parse(localStorage.getItem("user"))
        }
    }
    return fetch(API_PATH_AUTH + "docs/user", obj)
        .then(response => response.json())
}
