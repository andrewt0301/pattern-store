import {API_PATH_ADMIN, API_PATH_AUTH} from "./common";

export function deleteDocById(docId) {
    let obj = {
        method: "DELETE",
        mode: "cors",
        headers: {
            "Authorization": "Basic " + JSON.parse(localStorage.getItem("user"))
        }
        // body: 'A=1&B=2'
    }
    return fetch(API_PATH_ADMIN + "doc/" + docId + "/delete", obj)
}
