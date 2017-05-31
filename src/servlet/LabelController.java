package servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import hibernate.HibernateUtil;
import model.Label;

@Controller
public class LabelController {

	/*---------增加标签----------*/
	@RequestMapping("addlabel.do")
	public String AddLabel(@RequestParam(value="pid")String[] pid, @RequestParam(value="lname")String lname, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			for(int i = 0; i < pid.length; i++){
				Label label = new Label();
				label.setLname(lname);
				label.setPid(pid[i]);
				session.save(label);
			}
			session.getTransaction().commit();
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*---------删除标签----------*/
	@RequestMapping("deletelabel.do")
	public String DeleteLabel(@RequestParam(value="pid")String[] pid, @RequestParam(value="dlname")String lname, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			for(int i = 0; i < pid.length; i++){
				String hql = "from Label where pid=? and lname=?";
				Query query = session.createQuery(hql);
				query.setString(0, pid[i]);
				query.setString(1, lname);
				List<Label> labellist = query.list();
				for(int j = 0; j < labellist.size(); j++){
					Label label = new Label();
					label = labellist.get(j);
					if(label == null) 
						throw new Exception("标签不存在");
					session.delete(label);
				}
			}
			session.getTransaction().commit();
			return "admin";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*---------修改标签----------*/
	@RequestMapping("modlabel.do")
	public String ModLabel(@RequestParam(value="lid")String[] lid, @RequestParam(value="lname")String lname, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			for(int i = 0; i < lid.length; i++){
				Label label = new Label();
				label = (Label) session.get(Label.class, lid[i]);
				if(label == null) 
					throw new Exception("标签不存在");
				label.setLname(lname);
				session.update(label);
			}
			session.getTransaction().commit();
			return "success";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

	/*---------查询标签----------*/
	@RequestMapping("selectlabel.do")
	public String SelectLabel(@RequestParam(value="lname")String lname, HttpServletRequest request) {
		try {
			Session session = HibernateUtil.getSessionFactory().getCurrentSession();
			session.beginTransaction();
			String hql = "from Label where lname=?";
			Query query = session.createQuery(hql);
			query.setString(0, lname);
			List<Label> labellist = query.list();
			session.getTransaction().commit();
			request.setAttribute("objlist", labellist);
			return "success";
		} catch (Exception ex) {
			request.getSession().setAttribute("errormsg", ex.getMessage());
			return "login";
		}
	}

}
