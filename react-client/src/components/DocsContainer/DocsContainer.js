import Doc from "../Doc/Doc";
import {Link} from "react-router-dom";
import {DocActionColumn} from "../DocActionColumn/DocActionColumn";
import React from "react";

export function DocsContainer(props) {
    return (<div>
        <div className="text-dark m-3">
            <b>{props.title}</b>
        </div>
        <div className="container">
            <div className="row">
                <div className="col-sm-8">
                    {props.docs.map(
                        item =>
                            (
                                <div className="card m-2" key={item.id}>
                                    <Doc
                                        id={item.id}
                                        lang={item.lang}
                                        scenario={item.scenario}
                                        team={item.team}
                                        timestamp={item.timestamp}
                                        patterns={item.patterns}
                                    />
                                    <Link to={`/doc/${item.id}`}>
                                        <div className="btn btn-primary mb-2 mx-2">View</div>
                                    </Link>
                                </div>
                            )
                    )}
                </div>
                <div className="col-sm-3">
                    <DocActionColumn/>
                </div>
            </div>
        </div>
    </div>)
}
