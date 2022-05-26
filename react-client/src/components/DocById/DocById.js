import Doc from "../Doc/Doc";
import React, {useEffect, useState} from "react";
import {getCommonDocById} from "../../services/getCommonDocById";
import {Link} from "react-router-dom";
import {InputDocIdForm} from "../InputDocIdForm/InputDocIdForm";
import {DocActionColumn} from "../DocActionColumn/DocActionColumn";

export function DocById({
                            docId
                        }) {
    const [doc, setDoc] = useState([]);

    useEffect(() => {
        let mounted = true;
        docId && getCommonDocById(docId)
            .then(item => {
                if (mounted) {
                    setDoc(item)
                }
            })
        return () => mounted = false;
    }, [])

    if (docId === undefined) {
        return (<InputDocIdForm/>)
    }
    return (
        <div className="row">
            <div className="card col-8 mx-2">
                <div className="card-header">
                    <b>Document by id</b>
                </div>
                <div className="card-body">
                    <Doc
                        id={doc.id}
                        lang={doc.lang}
                        scenario={doc.scenario}
                        team={doc.team}
                        timestamp={doc.timestamp}
                        patterns={doc.patterns}
                    />
                </div>
                <div className="d-grid gap-2 d-md-block mb-2" aria-label="Buttons for actions">
                    <Link to={`/doc/${doc.id}/delete`}>
                        <div className="btn btn-primary mx-2 ">Delete</div>
                    </Link>
                    {/*<div className="btn btn-primary mx-2">Show patterns</div>*/}
                    <Link to={`/doc/${doc.id}/feedback/add`}>
                        <div className="btn btn-primary mx-2">Leave feedback</div>
                    </Link>
                    <Link to={`/doc/${doc.id}/feedback/view`}>
                        <div className="btn btn-primary mx-2">View feedback</div>
                    </Link>
                    <Link to={`/doc/${doc.id}/statistic`}>
                        <div className="btn btn-primary mx-2">Show statistic</div>
                    </Link>
                </div>
            </div>
            <div className="col-sm-3">
                <DocActionColumn/>
            </div>
        </div>
    );
}
