import {API_PATH} from "../common";

export function getDocDownloadCount(docId) {
    let obj = {
        method: "POST",
        mode: "cors",
        headers: {
            "Content-type": "application/json",
            "Accept": "application/json"
        },
        body: `{"ids": ["${docId.docId}"]}`
    }
    return fetch(API_PATH + "statistic/docs/downloads/count", obj)
        .then(response => response.json())
}
