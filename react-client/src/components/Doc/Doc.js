import React from 'react';
// import PropTypes from 'prop-types';
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
            <div className="btn btn-primary">View</div>
        </div>
    )
}
{/*<button onClick={() => showAdditional(additional)}>More Info</button>*/}

// AnimalCard.propTypes = {
//     additional: PropTypes.shape({
//         link: PropTypes.string,
//         notes: PropTypes.string
//     }),
//     diet: PropTypes.arrayOf(PropTypes.string).isRequired,
//     name: PropTypes.string.isRequired,
//     scientificName: PropTypes.string.isRequired,
//     showAdditional: PropTypes.func.isRequired,
//     size: PropTypes.number.isRequired,
// }

Doc.defaultProps = {
    scenario: {
        type: "Not specified"
    },
    team: "Not specified"
}
