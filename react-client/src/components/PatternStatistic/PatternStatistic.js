import React, {useEffect, useState} from "react";
import {DocActionColumn} from "../DocActionColumn/DocActionColumn";
import {UnmountClosed} from 'react-collapse';
import {getPtrnUsage} from "../../services/statistic/getPtrnUsage";
import {showUsages} from "../helpers/showUsages";

export function PatternStatistic(patternId) {
    const [ptrnUsage, setPtrnUsage] = useState([])
    const [ptrnUsageMerged, setPtrnUsageMerged] = useState([])

    const [isPtrnUsage, setIsPtrnUsage] = useState(false);
    const [isPtrnUsageMerged, setIsPtrnUsageMerged] = useState(false);

    useEffect(() => {
        let mounted = true;
        patternId && getPtrnUsage(patternId.patternId)
            .then(item => {
                if (mounted) {
                    // console.log(item)
                    setIsPtrnUsage(item.ptrnsStatas)
                }
            })
        return () => mounted = false;
    }, [])

    return (
        <div>
            <div className="text-dark m-3">
                <b>Statistic about pattern</b>
            </div>
            <div className="container">
                <div className="row">
                    <div className="col-sm-8">
                        <div className="my-2">
                            <input
                                type="checkbox"
                                id="ptrnUsage"
                                name="ptrnUsage"
                                value="Show statistic for usages of pattern"
                                checked={isPtrnUsage}
                                onChange={() => setIsPtrnUsage(!isPtrnUsage)}
                            />
                            <label className="mx-3" htmlFor="docUsage">Show statistic for usages of pattern</label>
                        </div>
                        <UnmountClosedPtrnUsage isPtrnUsage={isPtrnUsage} ptrnUsage={ptrnUsage}
                                                patternId={patternId.patternId}/>
                    </div>
                    <div className="col-sm-3">
                        <DocActionColumn/>
                    </div>
                </div>
            </div>
        </div>
    )
}

export function UnmountClosedPtrnUsage(props) {
    return (
        <UnmountClosed isOpened={props.isPtrnUsage}>
            <div>
                <h6>Pattern id: {props.patternId}</h6>
                {props.ptrnUsage && props.ptrnUsage.map((stata, idx) => (
                        <div key={idx}>
                            <p><b>Successful</b> usages of patterns. Entry: {idx + 1}</p>
                            {showUsages(stata.stataPtrns.success)}
                            <p><b>Failure</b> usages of patterns. Entry: {idx + 1}</p>
                            {showUsages(stata.stataPtrns.failure)}
                            <p><b>Downloads</b> of patterns. Entry: {idx + 1}</p>
                            {showUsages(stata.stataPtrns.download)}
                        </div>
                    )
                )}
            </div>
        </UnmountClosed>
    )
}
