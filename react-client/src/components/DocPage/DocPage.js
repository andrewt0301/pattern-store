import Doc from "../Doc/Doc";
import React, {useEffect, useState} from "react";
import {getCommonDocById} from "../../services/getCommonDocById";
import {getCommonDocs} from "../../services/getCommonDocs";

export function DocPage() {
    const [doc, setDoc] = useState([]);
    const [docs, setDocs] = useState([])
    const docId = "625748988af05121cc0d6189"

    useEffect(() => {
        let mounted = true;
        getCommonDocById(docId)
            .then(items => {
                if (mounted) {
                    setDoc(items)
                }
            })
        return () => mounted = false;
    }, [])
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
            <div className="card">
                <div className="card-header">
                    <b>Documents by id</b>
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
            </div>

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
                                    </div>
                                )
                        )}
                    </div>
                    <div className="list-group col-sm-3">
                        <button type="button" className="list-group-item list-group-item-action active">
                            Filter documents
                        </button>
                        <button type="button" className="list-group-item list-group-item-action">
                            Filter common documents
                        </button>
                        <button type="button" className="list-group-item list-group-item-action">
                            Get document by id
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
