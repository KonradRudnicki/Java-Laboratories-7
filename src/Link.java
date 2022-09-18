import java.util.Locale;
import java.util.Objects;

public class Link implements Comparable<Link> {
	public String ref;
	public int weight;

	public Link(String ref) {
		this.ref = ref.toLowerCase(Locale.ROOT);
		weight = 1;
	}

	public Link(String ref, int weight) {
		this.ref = ref;
		this.weight = weight;
	}

	public int getWeight() {
		return weight;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Link)) return false;
		Link link = (Link) o;

		return Objects.equals(ref, link.ref);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ref);
	}

	@Override
	public String toString() {
		return ref + "(" + weight + ")";
	}

	@Override
	public int compareTo(Link another) {
		return this.ref.compareTo(another.ref);
	}
}