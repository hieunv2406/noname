import React from "react";

interface IProps {
    people: {
        name: string,
        age: number,
        url: string,
        note?: string
    }[]
}

const List: React.FC<IProps> = ({ people }) => {

    const renderList = (): JSX.Element[] => {
        return people.map((person) => {
            return (
                <li>
                    <div>
                        <img src={person.url} />
                        <h2>{person.name}</h2>
                    </div>
                    <p>{person.age}</p>
                    <p>{person.note}</p>
                </li>
            )
        })
    }

    return (
        <div>
            I am a List
            <ul>
                {renderList()}
            </ul>
        </div>
    )
}

export default List