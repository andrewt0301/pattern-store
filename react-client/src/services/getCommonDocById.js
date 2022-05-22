import {API_PATH} from "./common";

export function getCommonDocById(docId) {
    let obj = {
        method: "GET",
        mode: "cors",
        headers: {
            "Accept": "application/json"
        }
    }
    return fetch(API_PATH + "doc/" + docId, obj)
        .then(response => response.json())
}
