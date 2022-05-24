import {API_PATH} from "../common";

export function addDocFeedback(feedback) {
    let obj = {
        method: "POST",
        mode: "cors",
        headers: {
            "Content-type": "application/json"
        },
        body: JSON.stringify(feedback)
    }
    return fetch(API_PATH + "feedback/add", obj)
}
