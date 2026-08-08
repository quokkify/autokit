export default {
  name: "Autokit",
  output: "./allure-report",
  plugins: {
    awesome: {
      options: {
        reportName: "Autokit test report",
        singleFile: false,
        reportLanguage: "en",
        groupBy: ["epic", "feature", "story"],
      },
    },
  },
};
