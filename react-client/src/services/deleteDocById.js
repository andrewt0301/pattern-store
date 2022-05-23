import {API_PATH_AUTH} from "./common";

export function deleteDocById(docId) {
    let obj = {
        method: "DELETE",
        mode: "cors",
        headers: {
            // 'Authorization': 'Basic '+btoa('username:password'),
            // 'Content-Type': 'application/json',
            "Accept": "application/json"
        }
        // body: 'A=1&B=2'
    }
    return fetch(API_PATH_AUTH + "doc/" + docId + "/delete", obj)
        .then(response => response.json())
}
