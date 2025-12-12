<template>
  <div class="shell">
    <h1>学生选课系统</h1>

    <div v-if="message" class="alert">{{ message }}</div>
    <div v-if="error" class="alert bad">{{ error }}</div>

    <div class="grid">
      <div class="card">
        <h2>新增学生</h2>
        <form @submit.prevent="addStudent">
          <label>姓名</label>
          <input v-model="studentForm.name" placeholder="张三" required />
          <label>邮箱</label>
          <input v-model="studentForm.email" placeholder="student@example.com" required />
          <button type="submit" :disabled="loading">保存学生</button>
        </form>
      </div>

      <div class="card">
        <h2>新增课程</h2>
        <form @submit.prevent="addCourse">
          <label>课程代码</label>
          <input v-model="courseForm.code" placeholder="CS101" required />
          <label>课程名称</label>
          <input v-model="courseForm.title" placeholder="计算机基础" required />
          <label>描述</label>
          <textarea v-model="courseForm.description" rows="3" placeholder="课程简介"></textarea>
          <label>容量</label>
          <input v-model.number="courseForm.capacity" type="number" min="1" />
          <button type="submit" :disabled="loading">保存课程</button>
        </form>
      </div>
    </div>

    <div class="card" style="margin-top:16px;">
      <h2>选课 / 退课</h2>
      <div class="grid">
        <div>
          <label>选择学生</label>
          <select v-model="enrollmentForm.studentId">
            <option disabled value="">请选择学生</option>
            <option v-for="s in students" :key="s.id" :value="s.id">
              {{ s.name }} ({{ s.email }})
            </option>
          </select>
        </div>
        <div>
          <label>选择课程</label>
          <select v-model="enrollmentForm.courseId">
            <option disabled value="">请选择课程</option>
            <option v-for="c in courses" :key="c.id" :value="c.id">
              {{ c.code }} - {{ c.title }}
            </option>
          </select>
        </div>
      </div>
      <div style="display:flex; gap:10px; margin-top:12px;">
        <button @click="enroll" :disabled="loading">确认选课</button>
        <button @click="drop" :disabled="loading">退课</button>
      </div>
    </div>

    <div class="grid" style="margin-top:16px;">
      <div class="card">
        <h2>学生列表</h2>
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>姓名</th>
              <th>邮箱</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="s in students" :key="s.id">
              <td>{{ s.id }}</td>
              <td>{{ s.name }}</td>
              <td>{{ s.email }}</td>
              <td style="display:flex; gap:6px;">
                <button @click="viewCoursesForStudent(s)" style="background:#0ea5e9;">查看课程</button>
                <button @click="removeStudent(s.id)" style="background:#ef4444;">删除</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <div class="card">
        <h2>课程列表</h2>
        <table class="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>代码</th>
              <th>名称</th>
              <th>容量</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="c in courses" :key="c.id">
              <td>{{ c.id }}</td>
              <td class="pill">{{ c.code }}</td>
              <td>{{ c.title }}</td>
              <td>{{ c.capacity }}</td>
              <td style="display:flex; gap:6px;">
                <button @click="viewStudentsForCourse(c)" style="background:#22c55e;">查看学生</button>
                <button @click="removeCourse(c.id)" style="background:#ef4444;">删除</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <div class="grid" style="margin-top:16px;">
      <div class="card">
        <h2>学生的课程</h2>
        <p class="muted" v-if="!selectedStudent">请选择学生查看课程</p>
        <p v-else class="muted">当前学生：{{ selectedStudent.name }}</p>
        <ul class="list">
          <li v-for="c in studentCourses" :key="c.id" class="row">
            <div>
              <div><strong>{{ c.code }}</strong> - {{ c.title }}</div>
              <div class="muted">{{ c.description }}</div>
            </div>
            <button @click="dropSpecific(selectedStudent.id, c.id)" style="max-width:120px;">退课</button>
          </li>
          <li v-if="selectedStudent && studentCourses.length === 0" class="muted">暂无课程</li>
        </ul>
      </div>

      <div class="card">
        <h2>课程的学生</h2>
        <p class="muted" v-if="!selectedCourse">请选择课程查看学生</p>
        <p v-else class="muted">当前课程：{{ selectedCourse.title }}</p>
        <ul class="list">
          <li v-for="s in courseStudents" :key="s.id" class="row">
            <div>
              <div><strong>{{ s.name }}</strong></div>
              <div class="muted">{{ s.email }}</div>
            </div>
            <button @click="dropSpecific(s.id, selectedCourse.id)" style="max-width:120px;">退课</button>
          </li>
          <li v-if="selectedCourse && courseStudents.length === 0" class="muted">暂无学生</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue';
import api from './api';

const students = ref([]);
const courses = ref([]);
const studentCourses = ref([]);
const courseStudents = ref([]);
const selectedStudent = ref(null);
const selectedCourse = ref(null);
const studentForm = ref({ name: '', email: '' });
const courseForm = ref({ code: '', title: '', description: '', capacity: 50 });
const enrollmentForm = ref({ studentId: '', courseId: '' });
const loading = ref(false);
const message = ref('');
const error = ref('');

onMounted(() => {
  loadStudents();
  loadCourses();
});

const clearAlerts = () => {
  message.value = '';
  error.value = '';
};

const handleError = (err) => {
  error.value = err?.response?.data?.error || err.message || '请求失败';
  loading.value = false;
};

const loadStudents = async () => {
  try {
    const { data } = await api.get('/students');
    students.value = data;
  } catch (err) {
    handleError(err);
  }
};

const loadCourses = async () => {
  try {
    const { data } = await api.get('/courses');
    courses.value = data;
  } catch (err) {
    handleError(err);
  }
};

const addStudent = async () => {
  try {
    clearAlerts();
    loading.value = true;
    const { data } = await api.post('/students', studentForm.value);
    students.value.push(data);
    studentForm.value = { name: '', email: '' };
    message.value = '学生已保存';
  } catch (err) {
    handleError(err);
  } finally {
    loading.value = false;
  }
};

const addCourse = async () => {
  try {
    clearAlerts();
    loading.value = true;
    const { data } = await api.post('/courses', courseForm.value);
    courses.value.push(data);
    courseForm.value = { code: '', title: '', description: '', capacity: 50 };
    message.value = '课程已保存';
  } catch (err) {
    handleError(err);
  } finally {
    loading.value = false;
  }
};

const enroll = async () => {
  if (!enrollmentForm.value.studentId || !enrollmentForm.value.courseId) {
    error.value = '请选择学生和课程';
    return;
  }
  try {
    clearAlerts();
    loading.value = true;
    await api.post('/enrollments', enrollmentForm.value);
    message.value = '选课成功';
    if (selectedStudent.value?.id === enrollmentForm.value.studentId) {
      await viewCoursesForStudent(selectedStudent.value);
    }
    if (selectedCourse.value?.id === enrollmentForm.value.courseId) {
      await viewStudentsForCourse(selectedCourse.value);
    }
  } catch (err) {
    handleError(err);
  } finally {
    loading.value = false;
  }
};

const drop = async () => {
  if (!enrollmentForm.value.studentId || !enrollmentForm.value.courseId) {
    error.value = '请选择学生和课程';
    return;
  }
  await dropSpecific(enrollmentForm.value.studentId, enrollmentForm.value.courseId);
};

const dropSpecific = async (studentId, courseId) => {
  try {
    clearAlerts();
    loading.value = true;
    await api.delete('/enrollments', { data: { studentId, courseId } });
    message.value = '退课成功';
    if (selectedStudent.value?.id === studentId) {
      await viewCoursesForStudent(selectedStudent.value);
    }
    if (selectedCourse.value?.id === courseId) {
      await viewStudentsForCourse(selectedCourse.value);
    }
  } catch (err) {
    handleError(err);
  } finally {
    loading.value = false;
  }
};

const viewCoursesForStudent = async (student) => {
  selectedStudent.value = student;
  try {
    const { data } = await api.get(`/students/${student.id}/courses`);
    studentCourses.value = data;
  } catch (err) {
    handleError(err);
  }
};

const viewStudentsForCourse = async (course) => {
  selectedCourse.value = course;
  try {
    const { data } = await api.get(`/courses/${course.id}/students`);
    courseStudents.value = data;
  } catch (err) {
    handleError(err);
  }
};

const removeStudent = async (id) => {
  try {
    clearAlerts();
    await api.delete(`/students/${id}`);
    students.value = students.value.filter((s) => s.id !== id);
    if (selectedStudent.value?.id === id) {
      selectedStudent.value = null;
      studentCourses.value = [];
    }
    message.value = '学生已删除';
  } catch (err) {
    handleError(err);
  }
};

const removeCourse = async (id) => {
  try {
    clearAlerts();
    await api.delete(`/courses/${id}`);
    courses.value = courses.value.filter((c) => c.id !== id);
    if (selectedCourse.value?.id === id) {
      selectedCourse.value = null;
      courseStudents.value = [];
    }
    message.value = '课程已删除';
  } catch (err) {
    handleError(err);
  }
};
</script>
