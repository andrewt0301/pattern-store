import {API_PATH_ADMIN} from "./common";

export function deleteDocById(docId) {
    let obj = {
        method: "DELETE",
        mode: "cors",
        headers: {
            "Authorization": "Basic " + JSON.parse(localStorage.getItem("user"))
        }
    }
    return fetch(API_PATH_ADMIN + "doc/" + docId + "/delete", obj)
}
