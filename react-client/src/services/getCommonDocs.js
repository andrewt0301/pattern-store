import {API_PATH} from "./common";

export function getCommonDocs() {
    let obj = {
        method: "POST",
        mode: "cors",
        headers: {
            // 'Authorization': 'Basic '+btoa('username:password'),
            // 'Content-Type': 'application/x-www-form-urlencoded'
            "Accept": "application/json"
        }
        // body: 'A=1&B=2'
    }
    return fetch(API_PATH + "docs/filtered", obj)
        .then(response => response.json())
}
