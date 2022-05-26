import {Table} from "react-bootstrap";
import React from "react";
import {Link} from "react-router-dom";

export function showUsages(usages, docActions) {
    return (
        <Table>
            <thead>
            <tr>
                <th scope="col">#</th>
                <th scope="col">Pattern id</th>
                <th scope="col">Count</th>
                {docActions && <th scope="col">Actions</th>}
            </tr>
            </thead>
            <tbody>
            {usages && usages.map(
                (usage, idx) => (
                    <tr key={idx}>
                        <th scope="row">{idx}</th>
                        <td>{usage.patternId}</td>
                        <td>{usage.count}</td>
                        {docActions && <td>
                            {/*<Link to={`/pattern/${usage.patternId}/statistic`}>*/}
                                <div className="btn btn-primary mx-1">Statistic</div>
                            {/*</Link>*/}
                            <div className="btn btn-primary mx-1">Merged statistic</div>
                        </td>
                        }
                    </tr>
                )
            )}
            </tbody>
        </Table>
    )
}
