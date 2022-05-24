import {API_PATH} from "../common";

export function getDocFeedbacks(docId) {
    let obj = {
        method: "GET",
        mode: "cors",
        headers: {
            "Accept": "application/json"
        }
    }
    return fetch(API_PATH + "feedback/doc/" + docId.docId, obj)
        .then(response => response.json())
}