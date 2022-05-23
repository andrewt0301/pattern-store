import Doc from "../Doc/Doc";
import React, {useEffect, useState} from "react";
import {getCommonDocs} from "../../services/getCommonDocs";
import {Link} from "react-router-dom";
import {DocActionColumn} from "../DocActionColumn/DocActionColumn";

export function DocsCommonPage() {
    const [docs, setDocs] = useState([])

    useEffect(() => {
        let mounted = true;
        getCommonDocs()
            .then(items => {
                if (mounted) {
                    setDocs(items)
                }
            })
        return () => mounted = false;
    }, [])
    return (
        <div>
            <div className="text-dark">
                <b>Common docs</b>
            </div>
            <div className="container">
                <div className="row">
                    <div className="col-sm-8">
                        {docs.map(
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
        </div>
    );
}
