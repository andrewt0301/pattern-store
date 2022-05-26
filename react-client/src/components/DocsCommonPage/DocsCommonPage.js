import React, {useEffect, useState} from "react";
import {getCommonDocs} from "../../services/getCommonDocs";
import {DocsContainer} from "../DocsContainer/DocsContainer";

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
        <DocsContainer title="Common documents" docs={docs}/>
    );
}
