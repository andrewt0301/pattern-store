import {deleteDocById} from "../../services/deleteDocById";

export function DocDelete(docId) {
    // const [answer, setAnswer] = useState([])
    // useEffect(() => {
    //     let mounted = true;
    //     docId && deleteDocById(docId)
    //         .then(answer => {
    //             if (mounted) {
    //                 setAnswer(answer)
    //             }
    //         })
    //     return () => mounted = false;
    // }, [])
    return docId && deleteDocById(docId.docId)
        .then(resp => {
            console.log(resp)
            console.log(resp.status)
            if (resp.status !== 200) {
                if (resp.status === 401) {
                    // return Promise.reject(
                    //     <p className="text-danger">You don't have permission to delete this document</p>
                    // )
                    return <p className="text-danger">You don't have permission to delete this document</p>
                }
                const error = (resp && resp.error) || resp.status;
                // return Promise.reject(<p className="text-danger">{error}</p>)
                return <p className="text-danger">{error}</p>
            }
            return <p className="text-info">Doc was successfully deleted</p>;
        })
}

function handleResponse(res) {
    console.log(res)
    if (res.status !== 200) {
        if (res.status === 401) {
            return <p className="text-danger">You don't have permission to delete this document</p>
        }
        const error = (res && res.error) || res.status;
        return <p className="text-danger">{error}</p>
    }
    return <p className="text-info">Doc was successfully deleted</p>;
}
