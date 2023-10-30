package com.ead.course.specifications;

import com.ead.course.models.Course;
import com.ead.course.models.Lesson;
import com.ead.course.models.Module;
import com.ead.course.models.User;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    @And({
            @Spec(path = "courseLevel", spec = Equal.class),
            @Spec(path = "courseStatus", spec = Equal.class),
            @Spec(path = "name", spec = Like.class)
    })
    public interface CourseSpec extends Specification<Course> {
    }

    @And({
            @Spec(path = "email", spec = Like.class),
            @Spec(path = "fullName", spec = Like.class),
            @Spec(path = "userStatus", spec = Equal.class),
            @Spec(path = "userType", spec = Equal.class),
    })
    public interface UserSpec extends Specification<User> {
    }

    @Spec(path = "title", spec = Like.class)
    public interface ModuleSpec extends Specification<Module> {
    }

    @Spec(path = "title", spec = Like.class)
    public interface LessonSpec extends Specification<Lesson> {
    }

    public static Specification<Module> moduleCourseId(final UUID courseId) {
        return (root, query, builder) -> {
            query.distinct(true);
            Root<Course> course = query.from(Course.class);
            Expression<Collection<Module>> coursesModules = course.get("modules");
            return builder.and(builder.equal(course.get("courseId"), courseId), builder.isMember(root, coursesModules));
        };
    }

    public static Specification<Lesson> lessonModuleId(final UUID moduleId) {
        return (root, query, builder) -> {
            query.distinct(true);
            Root<Module> module = query.from(Module.class);
            Expression<Collection<Lesson>> moduleLessons = module.get("lessons");
            return builder.and(builder.equal(module.get("moduleId"), moduleId), builder.isMember(root, moduleLessons));
        };
    }

    public static Specification<User> userCourseId(final UUID courseId) {
        return (root, query, builder) -> {
            query.distinct(true);
            Root<Course> course = query.from(Course.class);
            Expression<Collection<User>> coursesUsers = course.get("users");
            return builder.and(builder.equal(course.get("courseId"), courseId), builder.isMember(root, coursesUsers));
        };
    }

    public static Specification<Course> courseUserId(final UUID userId) {
        return (root, query, builder) -> {
            query.distinct(true);
            Root<User> user = query.from(User.class);
            Expression<Collection<Course>> usersCourses = user.get("courses");
            return builder.and(builder.equal(user.get("userId"), userId), builder.isMember(root, usersCourses));
        };
    }
}
