import {API_PATH} from "./common";

export function getCommonDocs() {
    let obj = {
        method: "POST",
        mode: "cors",
        headers: {
            "Accept": "application/json"
        }
    }
    return fetch(API_PATH + "docs/filtered", obj)
        .then(response => response.json())
}
