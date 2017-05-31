package servlet;

import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hibernate.HibernateUtil;
import model.Picture;
import model.Tag;

@Controller
public class TagController {

	/*-------修改添加标记----------*/
	@RequestMapping("addtagmod.do")
	public String AddTagMod(@RequestParam(value="pid")String pid, @RequestParam(value="xl")String[] xleft, @RequestParam(value="yl")String[] yleft, @RequestParam(value="xr")String[] xright, @RequestParam(value="yr")String[] yright, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			for(int i = 1; i < xleft.length; i++){
				Tag tag = new Tag();
				tag.setWid((String) request.getSession().getAttribute("ID"));
				tag.setPid(pid);
				tag.setXleft(Double.valueOf(xleft[i]));
				tag.setYleft(Double.valueOf(yleft[i]));
				tag.setXright(Double.valueOf(xright[i]));
				tag.setYright(Double.valueOf(yright[i]));
				session.save(tag);
			}
			Picture picture = (Picture) session.get(Picture.class, pid);
			if(picture == null) 
				throw new Exception("图片不存在");
			Timestamp time = new Timestamp(System.currentTimeMillis()); 
			time.setNanos(0);
			picture.setPmodtime(time);
			session.update(picture);
			session.getTransaction().commit();
			return "redirect:/selectnextpicture.do";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*----------检查添加标记------------*/
	@RequestMapping("addtagcheck.do")
	public String AddTagCheck(@RequestParam(value="pid")String pid, @RequestParam(value="delteList")String[] tid, @RequestParam(value="xl")String[] xleft, @RequestParam(value="yl")String[] yleft, @RequestParam(value="xr")String[] xright, @RequestParam(value="yr")String[] yright, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			if(xleft.length==1&&tid.length==1){
				Picture picture = (Picture) session.get(Picture.class, pid);
				if(picture == null) 
					throw new Exception("图片不存在");
				Timestamp time = new Timestamp(System.currentTimeMillis()); 
				time.setNanos(0);
				picture.setPchecktime(time);
				picture.setPcheckpeo((String) request.getSession().getAttribute("ID"));
				picture.setPok("YES");
				session.update(picture);
			}
			else{
				for(int i = 1; i < xleft.length; i++){
					Tag tag = new Tag();
					tag.setWid((String) request.getSession().getAttribute("ID"));
					tag.setPid(pid);
					tag.setXleft(Double.valueOf(xleft[i]));
					tag.setYleft(Double.valueOf(yleft[i]));
					tag.setXright(Double.valueOf(xright[i]));
					tag.setYright(Double.valueOf(yright[i]));
					session.save(tag);
				}
				for(int i = 1; i < tid.length; i++){
					Tag tag = new Tag();
					tag = (Tag) session.get(Tag.class, tid[i]);
					if(tag == null) 
						throw new Exception("标记不存在");
					session.delete(tag);
				}
				Picture picture = (Picture) session.get(Picture.class, pid);
				if(picture == null) 
					throw new Exception("图片不存在");
				Timestamp time = new Timestamp(System.currentTimeMillis()); 
				time.setNanos(0);
				picture.setPchecktime(time);
				picture.setPcheckpeo((String) request.getSession().getAttribute("ID"));
				picture.setPok("NO");
				session.update(picture);
			}
			session.getTransaction().commit();
			return "redirect:/selectnextpicturecheck.do";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*---------删除标记----------*/
	@RequestMapping("deletetag.do")
	public String DeleteTag(@RequestParam(value="tid")String[] tid, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			for(int i = 0; i < tid.length; i++){
				Tag tag = (Tag) session.get(Tag.class, tid[i]);
				if(tag == null) 
					throw new Exception("标记不存在");
				session.delete(tag);
			}
			session.getTransaction().commit();
			return "success";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*---------查询标记----------*/
	@RequestMapping("selecttag.do")
	public String SelectTag(@RequestParam(value="pid")String pid, HttpServletRequest request) {
		try {
			int sum = (int) request.getAttribute("sum");
			Picture picture = (Picture) request.getAttribute("picture");
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Tag where pid=?";
			Query query = session.createQuery(hql);
			query.setString(0, pid);
			List<Tag> taglist = query.list();
			session.getTransaction().commit();
			request.setAttribute("objlist", taglist);
			request.setAttribute("picture", picture);
			request.setAttribute("sum", sum);
			return "check";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

}
