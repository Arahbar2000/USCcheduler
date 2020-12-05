import { ListGroup } from "react-bootstrap"
import '../css/CourseItem.css'

const CourseItem = props => {
    const handleClick = () => {
        props.removeCourse(props.index);
    }

    return (
        <div className="courseItem">
            <ListGroup.Item>
                {props.course.department + props.course.courseNumber.toString()}
            </ListGroup.Item>
            <span onClick={handleClick} id="delete" >Delete</span>
        </div>
    )
}

export default CourseItem