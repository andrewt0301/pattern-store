import React, {useEffect, useState} from "react";
import {getPtrnUsageMerged} from "../../services/statistic/getPtrnUsageMerged";
import {DocActionColumn} from "../DocActionColumn/DocActionColumn";
import {Link} from "react-router-dom";
import {showUsagesMerged} from "../helpers/showUsagesMerged";

export function PatternStatisticMerged(patternId) {
    const [ptrnUsageMerged, setPtrnUsageMerged] = useState({})

    useEffect(() => {
        let mounted = true;
        patternId && getPtrnUsageMerged(patternId)
            .then(item => {
                if (mounted) {
                    setPtrnUsageMerged(item)
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
                        <PatternUsageMerged ptrnUsageMerged={ptrnUsageMerged}
                                            patternId={patternId.patternId}/>
                    </div>
                    <div className="col-sm-3">
                        <DocActionColumn/>
                        <div className="list-group mt-4">
                            <Link to={`/statistic/pattern/${patternId.patternId}`}>
                                <button type="button" className="list-group-item list-group-item-action">
                                    Show pure pattern statistic
                                </button>
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export function PatternUsageMerged(props) {
    return (
        <div>
            <h6>Pattern id: {props.patternId}</h6>
            <p><b>Successful</b> usages of patterns</p>
            {showUsagesMerged(props.ptrnUsageMerged.success)}
            <p><b>Failure</b> usages of patterns</p>
            {showUsagesMerged(props.ptrnUsageMerged.failure)}
            <p><b>Downloads</b> of patterns</p>
            {showUsagesMerged(props.ptrnUsageMerged.download)}
        </div>
    )
}
