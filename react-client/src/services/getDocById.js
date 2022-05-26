import {API_PATH, API_PATH_AUTH} from "./common";

export function getDocById(docId) {
    let userStrg = localStorage.getItem("user")
    let hdrs;
    let uri;
    if (userStrg === null) {
        hdrs = {"Accept": "application/json"}
        uri = API_PATH + "doc/"
    } else {
        hdrs = {
            "Accept": "application/json",
            "Authorization": "Basic " + JSON.parse(localStorage.getItem("user"))
        }
        uri = API_PATH_AUTH + "doc/"
    }
    let obj = {
        method: "GET",
        mode: "cors",
        headers: hdrs
    }
    return fetch(uri + docId, obj)
        .then(response => response.json())
}
