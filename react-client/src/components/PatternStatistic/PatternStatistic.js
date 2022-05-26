import React, {useEffect, useState} from "react";
import {DocActionColumn} from "../DocActionColumn/DocActionColumn";
import {getPtrnUsage} from "../../services/statistic/getPtrnUsage";
import {Link} from "react-router-dom";
import {showUsages} from "../helpers/showUsages";

export function PatternStatistic(patternId) {
    const [ptrnUsage, setPtrnUsage] = useState([])

    useEffect(() => {
        let mounted = true;
        patternId && getPtrnUsage(patternId)
            .then(item => {
                if (mounted) {
                    setPtrnUsage(item.ptrnsStatas)
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
                        <PatternUsage ptrnUsage={ptrnUsage}
                                      patternId={patternId.patternId}/>
                    </div>
                    <div className="col-sm-3">
                        <DocActionColumn/>
                        <div className="list-group mt-4">
                            <Link to={`/statistic/pattern/${patternId.patternId}/merged`}>
                                <button type="button" className="list-group-item list-group-item-action">
                                    Show merged pattern statistic
                                </button>
                            </Link>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export function PatternUsage(props) {
    return (
        <div>
            <h6>Pattern id: {props.patternId}</h6>
            {props.ptrnUsage && props.ptrnUsage.map((stata, idx) => (
                    <div key={idx}>
                        <p><b>Successful</b> usages of patterns. Entry: {idx + 1}</p>
                        {showUsages(stata.success)}
                        <p><b>Failure</b> usages of patterns. Entry: {idx + 1}</p>
                        {showUsages(stata.failure)}
                        <p><b>Downloads</b> of patterns. Entry: {idx + 1}</p>
                        {showUsages(stata.download)}
                    </div>

                )
            )}
        </div>
    )
}
