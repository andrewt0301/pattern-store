import React, {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import {getDocFeedbacks} from "../../services/feedback/getDocFeedbacks";

export function DocFeedbackView(docId) {
    const [feedbacks, setFeedbacks] = useState([])

    useEffect(() => {
        let mounted = true;
        getDocFeedbacks(docId)
            .then(items => {
                if (mounted) {
                    setFeedbacks(items.feedbackDtos)
                }
            })
        return () => mounted = false;
    }, [])
    return (
        <div>
            <div className="text-dark m-3">
                <b>Feedback about document</b>
            </div>
            <div className="container">
                <div className="row">
                    <div className="col-sm-8">
                        {feedbacks.map(
                            item => (
                                <div className="card m-2" key={item.id}>
                                    <div className="card-body">
                                        <div className="card-header">
                                            <b>Document id:</b> {item.docId}
                                        </div>
                                        <div className="card-text">
                                            {item.text && <div><b>Feedback:</b> {item.text}</div>}
                                        </div>
                                    </div>
                                    <Link to={`/doc/${item.docId}`}>
                                        <div className="btn btn-primary mb-2 mx-2">Go to doc</div>
                                    </Link>
                                </div>
                            )
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}
