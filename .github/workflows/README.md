# GitHub Actions Workflows

This directory contains automated workflows for the HRMS Application.

## 📋 Available Workflows

### 1. CI - Build and Test (`ci-build-test.yml`)
**Trigger:** Push to master/main/develop, Pull Requests, Manual

**What it does:**
- ✅ Builds the application with Maven
- ✅ Runs all tests
- ✅ Verifies database connectivity
- ✅ Runs comprehensive verification tests
- ✅ Uploads build artifacts

**Status Badge:**
```markdown
![CI Build](https://github.com/Vikaskumarjvk007/HRMS_CAPSTONE_PROJECT/actions/workflows/ci-build-test.yml/badge.svg)
```

---

### 2. Payroll Processing (`Payroll.yml`)
**Trigger:** 
- Monthly schedule (1st of each month at midnight)
- Manual trigger with custom month

**What it does:**
- ✅ Sets up MySQL database with sample data
- ✅ Generates monthly payroll for all employees
- ✅ Creates detailed payslips
- ✅ Commits payroll.log back to repository
- ✅ Uploads artifacts

**Manual Run:**
Go to Actions → Payroll CI → Run workflow → Select month (optional)

---

### 3. Code Quality Analysis (`code-quality.yml`)
**Trigger:** Push to master/main, Pull Requests, Manual

**What it does:**
- ✅ Runs code quality checks
- ✅ Performs security vulnerability scans
- ✅ Generates dependency analysis
- ✅ Uploads quality reports

---

### 4. Deploy Documentation (`deploy-docs.yml`)
**Trigger:** Push to master/main (when .md files change), Manual

**What it does:**
- ✅ Collects all documentation
- ✅ Creates documentation index
- ✅ Uploads documentation artifacts

---

### 5. Create Release (`release.yml`)
**Trigger:** 
- Push tags matching v*.*.* pattern
- Manual trigger with version input

**What it does:**
- ✅ Builds production-ready JAR
- ✅ Runs full test suite
- ✅ Creates release package with:
  - Application JAR
  - Database scripts
  - Documentation
  - Verification reports
- ✅ Creates GitHub Release
- ✅ Uploads release artifacts

**Create a Release:**
```bash
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

Or use manual workflow trigger.

---

## 🚀 Quick Start Guide

### View Workflow Status
1. Go to your repository
2. Click on "Actions" tab
3. See all workflow runs and their status

### Run Manual Workflows
1. Go to Actions tab
2. Select the workflow you want to run
3. Click "Run workflow" button
4. Fill in any required inputs
5. Click "Run workflow"

### Add Status Badges to README
Add these badges to your main README.md:

```markdown
![CI Build](https://github.com/Vikaskumarjvk007/HRMS_CAPSTONE_PROJECT/actions/workflows/ci-build-test.yml/badge.svg)
![Payroll](https://github.com/Vikaskumarjvk007/HRMS_CAPSTONE_PROJECT/actions/workflows/Payroll.yml/badge.svg)
![Code Quality](https://github.com/Vikaskumarjvk007/HRMS_CAPSTONE_PROJECT/actions/workflows/code-quality.yml/badge.svg)
```

---

## 📊 Workflow Dependencies

All workflows use:
- **Java**: Temurin JDK 21
- **Database**: MySQL 8.0
- **Build Tool**: Maven
- **OS**: Ubuntu Latest

---

## 🔧 Configuration

### Environment Variables
Workflows use these environment variables:
- `DB_URL`: jdbc:mysql://127.0.0.1:3306/hrms_project
- `DB_USER`: root
- `DB_PASSWORD`: root
- `CI`: true

### Secrets (if needed)
No secrets currently required. All workflows use MySQL service container.

---

## 📝 Workflow Features

### Automatic Features
- ✅ Dependency caching (faster builds)
- ✅ Artifact uploads (preserve build outputs)
- ✅ Concurrent execution control
- ✅ Health checks for services
- ✅ Detailed summaries in workflow runs

### Security
- ✅ Read-only permissions by default
- ✅ Write permissions only when needed
- ✅ Security vulnerability scanning
- ✅ Dependency auditing

---

## 🐛 Troubleshooting

### Workflow Fails at MySQL Step
- Check MySQL service health
- Verify wait time is sufficient
- Check database credentials

### Build Fails
- Check Java version (should be 21)
- Verify Maven cache
- Check pom.xml dependencies

### Test Failures
- Review test logs in artifacts
- Check database schema is loaded
- Verify sample data exists

---

## 📦 Artifacts

Each workflow produces artifacts that are stored for:
- **Build artifacts**: 30 days
- **Test results**: 7 days
- **Documentation**: 90 days
- **Release packages**: 90 days

Download artifacts from the workflow run page.

---

## 🎯 Best Practices

1. **Pull Requests**: CI runs automatically on PRs
2. **Releases**: Use semantic versioning (v1.0.0)
3. **Payroll**: Runs automatically monthly, check logs
4. **Manual Runs**: Available for testing/debugging

---

## 📚 Additional Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven on GitHub Actions](https://docs.github.com/en/actions/guides/building-and-testing-java-with-maven)
- [MySQL Service Container](https://docs.github.com/en/actions/using-containerized-services/creating-mysql-service-containers)

---

*Last Updated: October 15, 2025*
