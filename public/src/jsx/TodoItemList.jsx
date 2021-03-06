import React from 'react';
import TodoItem from './TodoItem.jsx';

/**
 * A filterable list of journal entries
 *
 */
export default class TodoItemList extends React.Component {
    render() {

        let filterText = this.props.filter.toLowerCase();

        return (
            <div className="">
                {
                    this.props.items
                        .filter(item => item.text.toLowerCase().includes(filterText))
                        .map(item => (
                            <TodoItem
                                key={item._id.$oid}
                                item={item}
                                highlightText={filterText}
                                handleDelete={() => this.props.handleDelete(item._id.$oid)}
                                handlePublic={() => this.props.handlePublic(item._id.$oid, !item.public)}
                            />
                        ))}
            </div>
        );
    }
}