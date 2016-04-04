import apt, sys

class Installer(object):
	def __init__(self):
		self.cache = apt.cache.Cache()

	def install_pkg(self, pkg_name):
		pkg = self.cache[pkg_name]
		if pkg.is_installed:
		    print "{pkg_name} already installed".format(pkg_name=pkg_name)
		else:
		    pkg.mark_install()
		    try:
		        self.cache.commit()
		    except Exception, arg:
		        print >> sys.stderr, "Sorry, package installation failed [{err}]".format(err=str(arg))
		        return False
		return True