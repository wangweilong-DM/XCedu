package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author Wang
 * @Version 1.0
 * @date 2019/8/31 9:07
 */
@Service
public class CourseService {


    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    CourseMarketRepository courseMarketRepository;

    @Autowired
    CourseMarketMapper courseMarketMapper;

    @Autowired
    FileSystemRepository fileSystemRepository;

    @Autowired
    CoursePicRepository coursePicRepository;

    @Autowired
    CmsPageClient cmsPageClient;

    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String publish_previewUrl;

    /**
     * 课程计划查询
     *
     * @param courseId 课程id
     * @return
     */
    public TeachplanNode fingTeachPlanList(String courseId) {
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);

        if (teachplanNode == null) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        return teachplanNode;

    }

    /**
     * 添加课程计划
     *
     * @param teachplan
     * @return
     */
    public ResponseResult addTeachplan(Teachplan teachplan) {

        if (teachplan == null ||
                StringUtils.isEmpty(teachplan.getCourseid()) ||
                StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }

        //课程id
        String courseid = teachplan.getCourseid();

        //父节点id
        String parentid = teachplan.getParentid();

        if (StringUtils.isEmpty(parentid)) {
            //如果父节点为空则取出根节点
            parentid = this.getTeachplanRoot(courseid);
        }
        Optional<Teachplan> optional = teachplanRepository.findById(parentid);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        //父节点
        Teachplan teachplanParent = optional.get();
        String parentGrade = teachplanParent.getGrade();
        //设置父节点
        teachplan.setParentid(parentid);
        teachplan.setStatus("0");//未发布

        //子节点的级别，根据父节点来判断
        if (parentGrade.equals("1")) {
            teachplan.setGrade("2");
        }
        if (parentGrade.equals("2")) {
            teachplan.setGrade("3");
        }
        //设置课程id
        teachplan.setCourseid(courseid);
        teachplanRepository.save(teachplan);

        ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);
        return responseResult;
    }

    //添加课程根节点，如果没有则手动配置
    private String getTeachplanRoot(String courseId) {
        //校验根节点
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();
        //取出课程计划根节点
        List<Teachplan> list = teachplanRepository.findByCourseidAndParentid(courseId, "0");


        if (list == null) {
            //新增一个根节点
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseId);
            teachplan.setPname(courseBase.getName());
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setStatus("0");
            teachplanRepository.save(teachplan);
            return teachplan.getId();

        }

        Teachplan teachplan = list.get(0);
        return teachplan.getId();
    }

    @Transactional
    public QueryResponseResult findCourseList(int page, int size, CourseListRequest courseListRequest) {
        PageHelper.startPage(page, size);
        // Page<CourseBase> courseList = courseMapper.findCourseList();
        Page<CourseOff> courseOffs = courseMapper.findCourseListAndPic();
        List<CourseOff> result = courseOffs.getResult();

        for (CourseOff res : result) {
            String pic = res.getPic();
            if (pic != null) {
                String substring = pic.substring(pic.indexOf("/") + 1);
                res.setPic(substring);
            }

        }
        // List<CourseBase> result = courseList.getResult();
        long total = courseOffs.getTotal();
        // long total = courseList.getTotal();
        QueryResult queryResult = new QueryResult();
        queryResult.setList(result);
        queryResult.setTotal(total);
//        queryResult.setTotal();
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS, queryResult);

        return queryResponseResult;

    }

    //级联选择器查询课程分类
    public CategoryNode findCategoryList() {

        CategoryNode categoryList = categoryMapper.findCategoryList();


        return categoryList;
    }


    @Transactional
    public AddCourseResult addCourseBase(CourseBase courseBase) {

        courseBase.setStatus("202001");

        CourseBase save = courseBaseRepository.save(courseBase);
        //String courseBaseId = courseBase.getId();
        AddCourseResult addCourseResult = new AddCourseResult(CommonCode.SUCCESS, courseBase.getId());
        return addCourseResult;
    }

    @Transactional
    public CourseBase getCourseBaseById(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        CourseBase courseBase = optional.get();
        return courseBase;
    }

    @Transactional
    public ResponseResult updateCourseBase(String id, CourseBase courseBase) {

       /* if (id == null || courseBase == null) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }*/
        Optional<CourseBase> optional = courseBaseRepository.findById(id);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }

        CourseBase courseBase1 = optional.get();
        courseBase1.setStatus(courseBase.getStatus());
        courseBase1.setDescription(courseBase.getDescription());
        courseBase1.setGrade(courseBase.getGrade());
        courseBase1.setName(courseBase.getName());
        courseBase1.setUsers(courseBase.getUsers());
        courseBase1.setSt(courseBase.getSt());
        courseBase1.setStudymodel(courseBase.getStudymodel());

        CourseBase save = courseBaseRepository.save(courseBase1);

        ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);

        return responseResult;
    }

    @Transactional
    public CourseMarket getCourseMarketById(String courseId) {
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        Optional<CourseMarket> optional = courseMarketRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseMarket courseMarket = optional.get();

        return courseMarket;
    }


    @Transactional
    //修改课程营销信息
    public ResponseResult updateCourseMarket(String courseId, CourseMarket courseMarket) {
        CourseMarket courseMarketById = getCourseMarketById(courseId);
        if (courseMarketById != null) {
            courseMarketById.setCharge(courseMarket.getCharge());
            courseMarketById.setPrice(courseMarket.getPrice());
            courseMarketById.setValid(courseMarket.getValid());
            courseMarketById.setEndTime(courseMarket.getEndTime());
//            courseMarketById.setPrice_old(courseMarket.getPrice_old());
            courseMarketById.setQq(courseMarket.getQq());
            courseMarketById.setStartTime(courseMarket.getStartTime());
            courseMarketRepository.save(courseMarketById);
        } else {
            courseMarketById = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, courseMarketById);
            courseMarketById.setId(courseId);
            courseMarketRepository.save(courseMarketById);
        }

        ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);
        return responseResult;
    }

    public ResponseResult addCoursePic(String courseId, String pic) {

        CoursePic coursePic = new CoursePic();
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        CoursePic save = coursePicRepository.save(coursePic);
        ResponseResult responseResult = new ResponseResult(CommonCode.SUCCESS);

        return responseResult;
    }

    public CoursePic findCoursePic(String courseId) {

        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (!optional.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        CoursePic coursePic = optional.get();
        String pic = coursePic.getPic();
        String substring = pic.substring(pic.indexOf("/") + 1);
        coursePic.setPic(substring);

        return coursePic;
    }

    /**
     * 删除图片
     *
     * @param courseId
     * @return
     */
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {

        //如果rsult>0 ,那么删除成功
        long result = coursePicRepository.deleteByCourseid(courseId);
        if (result > 0) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);

    }

    /**
     * 查询课程视图
     *
     * @param courseId
     * @return
     */
    public CourseView courseView(String courseId) {

        CourseView courseView = new CourseView();
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (!optional.isPresent()) {
        }
        CoursePic coursePic = optional.get();
        Optional<CourseBase> optional1 = courseBaseRepository.findById(courseId);
        if (!optional1.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CDETAILERROR);
        }
        CourseBase courseBase = optional1.get();
        Optional<CourseMarket> optional2 = courseMarketRepository.findById(courseId);
        if (!optional2.isPresent()) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CDETAILERROR);
        }
        CourseMarket courseMarket = optional2.get();
        //List<Teachplan> teachplans = teachplanRepository.findByCourseid(courseId);
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        courseView.setCoursePic(coursePic);
        courseView.setCourseBase(courseBase);
        courseView.setCourseMarket(courseMarket);
        courseView.setTeachplanNode(teachplanNode);
        return courseView;
    }

    /**
     * 课程预览功能开发
     *
     * @param courseId
     * @return
     */
    public CoursePublishResult preview(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        CourseBase courseBase = optional.get();
        //1.保存页面信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase(courseBase.getName());
        CmsPageResult cmsPageResult = cmsPageClient.saveCmsPage(cmsPage);
        if (!cmsPageResult.isSuccess()) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //2.拼装dataUrl
        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String dataUrl = publish_previewUrl + cmsPage1.getPageId();

        //返回CoursePublishResult

        CoursePublishResult coursePublishResult = new CoursePublishResult(CommonCode.SUCCESS, dataUrl);
        return coursePublishResult;
    }


    /**
     * 课程发布功能开发
     *
     * @param courseId
     * @return
     */
    @Transactional
    public CoursePublishResult publishCourse(String courseId) {
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        CourseBase courseBase = optional.get();
        courseBase.setStatus("202002");
        CourseBase save = courseBaseRepository.save(courseBase);
        //1.保存页面信息
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setDataUrl(publish_dataUrlPre + courseId);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPageName(courseId + ".html");
        cmsPage.setPageAliase(courseBase.getName());
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        String pageUrl = cmsPostPageResult.getPageUrl();
       CoursePublishResult coursePublishResult = new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
        return coursePublishResult;
    }
}