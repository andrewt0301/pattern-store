import React from 'react';
import './Doc.css'

export default function Doc({
                                id,
                                lang,
                                scenario,
                                team,
                                timestamp,
                                patterns,
                                // showAdditional,
                            }) {
    return (
        <div className="card-body">
            <div className="card-header">
                <b>Document id:</b> {id}
            </div>
            <div className="card-text">
                <b>Language</b>: {lang}
                {scenario && <div><b>Scenario:</b> {scenario.type}</div>}
                {team && <p><b>Team:</b> {team.name}</p>}
            </div>
            <div>{patterns?.map(ptrn => ptrn.id).join(', ')}</div>
            <div className="card-footer text-muted">
                {timestamp && <p><b>Timestamp:</b> {timestamp}</p>}
            </div>
        </div>
    )
}

Doc.defaultProps = {
    scenario: {
        type: "Not specified"
    },
    team: "Not specified"
}
