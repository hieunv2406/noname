import React from "react";
import Notice from "../../../partials/content/Notice";
import CodeExample from "../../../partials/content/CodeExample";
import { Table } from "react-bootstrap";

export default class EmployeeManagerPage extends React.Component {
  render() {
    return (
      <>
        <Notice icon="flaticon-warning kt-font-primary">
          For more info please check the components's official{" "}
          <a
            target="_blank"
            className="kt-link"
            rel="noopener noreferrer"
            href="https://react-bootstrap.github.io/components/table/"
          >
            demos & documentation
          </a>
        </Notice>

        <div className="row">
          <div className="col-md-12">
            <CodeExample jsCode={""} beforeCodeTitle="Breakpoint specific">
              <div className="kt-section">
                <span className="kt-section__sub">
                  Use <code>responsive="sm"</code>, <code>responsive="md"</code>
                  , <code>responsive="lg"</code>, or{" "}
                  <code>responsive="xl"</code> as needed to create responsive
                  tables up to a particular breakpoint. From that breakpoint and
                  up, the table will behave normally and not scroll
                  horizontally.
                </span>
                <div className="kt-separator kt-separator--dashed"></div>
                <div>
                  <Table responsive="md">
                    <thead>
                      <tr>
                        <th>#</th>
                        <th>Table heading</th>
                        <th>Table heading</th>
                        <th>Table heading</th>
                        <th>Table heading</th>
                        <th>Table heading</th>
                        <th>Table heading</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>1</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                      </tr>
                      <tr>
                        <td>2</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                      </tr>
                      <tr>
                        <td>3</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                        <td>Table cell</td>
                      </tr>
                    </tbody>
                  </Table>
                </div>
              </div>
            </CodeExample>
          </div>
        </div>
      </>
    );
  }
}