import {useState} from "react";
import {addDocFeedback} from "../../services/feedback/addDocFeedback";
import {Link, Navigate} from "react-router-dom";

export function DocFeedbackAdd(docId) {
    const [input, setInput] = useState()
    return (
        <div>
            <h5 className="m-3">Write feedback about document</h5>
            <div className="input-group w-50">
                <div className="input-group-prepend">
                    <span className="input-group-text">Feedback</span>
                </div>
                <textarea className="form-control"
                          aria-label="Feedback"
                          onChange={(event => {
                              setInput(event.target.value)
                          })}/>
            </div>
            <Link to={"/doc/" + docId.docId}>
                <div className="btn btn-primary m-3"
                     onClick={() => {
                         let obj = {
                             text: input,
                             userId: JSON.parse(localStorage.getItem("userId")),
                             docId: docId.docId
                         }
                         return addDocFeedback(obj)
                     }}>
                    Send feedback
                </div>
            </Link>
        </div>
    )
}
