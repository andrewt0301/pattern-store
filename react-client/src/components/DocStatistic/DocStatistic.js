import React, {useEffect, useState} from "react";
import {DocActionColumn} from "../DocActionColumn/DocActionColumn";
import {getDocDownloadCount} from "../../services/statistic/getDocDownloadCount";
import {UnmountClosed} from 'react-collapse';
import {getDocUsage} from "../../services/statistic/getDocUsage";
import {Table} from "react-bootstrap";
import {getDocUsageMerged} from "../../services/statistic/getDocUsageMerged";
import {showUsages} from "../helpers/showUsages";
import {showUsagesMerged} from "../helpers/showUsagesMerged";

export function DocStatistic(docId) {
    const [docDownloadCount, setDocDownloadCount] = useState([0])
    const [docUsage, setDocUsage] = useState([])
    const [docUsageMerged, setDocUsageMerged] = useState({})

    const [isDocUsage, setIsDocUsage] = useState(false);
    const [isDocUsageMerged, setIsDocUsageMerged] = useState(false);

    useEffect(() => {
        let mounted = true;
        docId && getDocDownloadCount(docId)
            .then(item => {
                if (mounted) {
                    setDocDownloadCount(Object.values(item.docsDownloads)[0])
                }
            })
        return () => mounted = false;
    }, [])

    useEffect(() => {
        let mounted = true;
        docId && getDocUsageMerged(docId)
            .then(item => {
                if (mounted) {
                    setDocUsageMerged(item)
                }
            })
        return () => mounted = false;
    }, [])

    useEffect(() => {
        let mounted = true;
        docId && getDocUsage(docId)
            .then(item => {
                if (mounted) {
                    setDocUsage(item.docStatas)
                }
            })
        return () => mounted = false;
    }, [])

    return (
        <div>
            <div className="text-dark m-3">
                <b>Statistic about document</b>
            </div>
            <div className="container">
                <div className="row">
                    <div className="col-sm-8">
                        <h5>Document download count: {docDownloadCount}</h5>
                        <div className="my-2">
                            <input
                                type="checkbox"
                                id="docUsage"
                                name="docUsage"
                                value="Show statistic for usages of doc"
                                checked={isDocUsage}
                                onChange={() => setIsDocUsage(!isDocUsage)}
                            />
                            <label className="mx-3" htmlFor="docUsage">Show document usage</label>
                        </div>
                        <UnmountClosedDocUsage isDocUsage={isDocUsage} docUsage={docUsage}/>
                        <div className="my-2">
                            <input
                                type="checkbox"
                                id="docUsageMerged"
                                name="docUsageMerged"
                                value="Show merged statistic for usages of doc"
                                checked={isDocUsageMerged}
                                onChange={() => setIsDocUsageMerged(!isDocUsageMerged)}
                            />
                            <label className="mx-3" htmlFor="docUsage">Show document usage merged</label>
                        </div>
                        <UnmountClosedDocUsageMerged isDocUsageMerged={isDocUsageMerged}
                                                     docUsageMerged={docUsageMerged}/>
                    </div>
                    <div className="col-sm-3">
                        <DocActionColumn/>
                    </div>
                </div>
            </div>
        </div>
    )
}

export function UnmountClosedDocUsage(props) {
    return (
        <UnmountClosed isOpened={props.isDocUsage}>
            <div>
                <h6>Doc id: {props.docUsage && props.docUsage[0]?.documentId}</h6>
                {props.docUsage.map((stata, idx) => (
                        <div key={idx}>
                            <p><b>Successful</b> usages of patterns. Entry: {idx + 1}</p>
                            {showUsages(stata.stataPtrns.success, true)}
                            <p><b>Failure</b> usages of patterns. Entry: {idx + 1}</p>
                            {showUsages(stata.stataPtrns.failure, true)}
                            <p><b>Downloads</b> of patterns. Entry: {idx + 1}</p>
                            {showUsages(stata.stataPtrns.download, true)}
                        </div>
                    )
                )}
            </div>
        </UnmountClosed>
    )
}

export function UnmountClosedDocUsageMerged(props) {
    return (
        <UnmountClosed isOpened={props.isDocUsageMerged}>
            <div>
                <h6>Doc id: {props.docUsageMerged && props.docUsageMerged[0]?.documentId}</h6>
                <p><b>Successful</b> usages of patterns</p>
                {showUsagesMerged(props.docUsageMerged.success, true)}
                <p><b>Failure</b> usages of patterns</p>
                {showUsagesMerged(props.docUsageMerged.failure, true)}
                <p><b>Downloads</b> of patterns</p>
                {showUsagesMerged(props.docUsageMerged.download, true)}
            </div>
        </UnmountClosed>
    )
}
