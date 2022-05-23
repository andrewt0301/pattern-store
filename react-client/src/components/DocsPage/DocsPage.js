import Doc from "../Doc/Doc";
import React, {useEffect, useState} from "react";
import {getCommonDocs} from "../../services/getCommonDocs";
import {Link} from "react-router-dom";

export function DocsPage() {
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
                    <div className="list-group col-sm-3">
                        <Link to="/docs">
                            <button type="button" className="list-group-item list-group-item-action">
                                Filter documents
                            </button>
                        </Link>
                        <Link to="/docs/filtered">
                            <div className="list-group-item list-group-item-action">
                                Filter common documents
                            </div>
                        </Link>
                        <Link to="/doc">
                            <button type="button" className="list-group-item list-group-item-action">
                                Get document by id
                            </button>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    );
}
