package models.filters;


import java.io.Serializable;

import javax.persistence.criteria.Expression;

import org.hibernate.jpa.criteria.CriteriaBuilderImpl;
import org.hibernate.jpa.criteria.ParameterRegistry;
import org.hibernate.jpa.criteria.Renderable;
import org.hibernate.jpa.criteria.compile.RenderingContext;
import org.hibernate.jpa.criteria.expression.LiteralExpression;
import org.hibernate.jpa.criteria.predicate.AbstractSimplePredicate;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

public class WithinPredicate extends AbstractSimplePredicate implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2678878694400400969L;
	private final Expression<Point> matchExpression;
    private final Expression<Geometry> area;

    public WithinPredicate(CriteriaBuilderImpl criteriaBuilder, Expression<Point> matchExpression, Geometry area) {
        this(criteriaBuilder, matchExpression, new LiteralExpression<Geometry>(criteriaBuilder, area));
    }
    public WithinPredicate(CriteriaBuilderImpl criteriaBuilder, Expression<Point> matchExpression, Expression<Geometry> area) {
        super(criteriaBuilder);
        this.matchExpression = matchExpression;
        this.area = area;
    }

    public Expression<Point> getMatchExpression() {
        return matchExpression;
    }

    public Expression<Geometry> getArea() {
        return area;
    }

    public void registerParameters(ParameterRegistry registry) {
        // Nothing to register
    }

    @Override
    public String render(boolean isNegated, RenderingContext renderingContext) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(" within(")
                .append(((Renderable) getMatchExpression()).render(renderingContext))
                .append(", ")
                .append(((Renderable) getArea()).render(renderingContext))
                .append(") = true ");
        return buffer.toString();
    }
}